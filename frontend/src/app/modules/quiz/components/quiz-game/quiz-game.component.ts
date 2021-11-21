import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Quiz, QuizType } from 'src/app/models/quiz';
import { User } from 'src/app/models/user';
import { UserService } from 'src/app/shared/services/users.service';

@Component({
  selector: 'app-quiz-game',
  templateUrl: './quiz-game.component.html',
  styleUrls: ['./quiz-game.component.scss']
})
export class QuizGameComponent implements OnInit {

  quizTitle: string;
  quizData: Quiz;
  questionTypes = QuizType
  user: User;
  currentQuiz = 0;
  quizPassed = false;
  correctAnswer: any = [];
  showErrors = false;

  constructor(
    private route: ActivatedRoute,
    private http: HttpClient,
    private userService: UserService,
    private toastr: ToastrService
  ) { }

  ngOnInit(): void {
    this.getUser();
    this.route.paramMap.subscribe((quiz: any) => this.quizTitle = quiz.params.id);
    this.http.get(`quiz/game/${this.quizTitle}`).subscribe((quiz: Quiz) => this.quizData = quiz);
  }

  previous(): void {
    if (this.currentQuiz >= 1) { this.currentQuiz--; }
    this.changeCurrentQuiz();
  }

  next(): void {
    if (this.currentQuiz <= 8) { this.currentQuiz++; }
    this.changeCurrentQuiz();
  }

  changeAnswer(index, quiz): void {
    let bool = quiz.every(v => v.answer == "FALSE"); // true если все false
    if (bool || quiz[index].answer==="TRUE") {
      if(quiz[index].answer == "TRUE") {
        quiz[index].answer = "FALSE";
      }
      else if(quiz[index].answer == "FALSE") {
        quiz[index].answer = "TRUE";
      }
    }
  }

  getUser(): void {
    this.userService.userInfo$.subscribe((user) => this.user = user);
  }

  logOut() {
    localStorage.removeItem('access_token');
    sessionStorage.removeItem('access_token');
  }

  // костыль поменяй 
  changeCurrentQuiz(): void {
    this.showErrors = false;    
  }

  onSubmit(): void {    
    this.correctAnswer = this.quizData.questions.map(quiz => {
      let firstAnswer = quiz.answers.filter((quiz: any) => quiz.answer == "TRUE")
      return firstAnswer[0];
    });

    let searchError = this.correctAnswer.every(data => data?.answer=="TRUE"); //проверка на пустоту    
    console.log(searchError);

    if (searchError) {
      let data = Object.assign({ user: this.user }, { quizTitle: this.quizTitle }, { answers: this.correctAnswer });
      
      this.http.post('quiz/game/end', data).subscribe(v => {  
        console.log(v);
        (this.quizData.questions as any) = v;        
        this.quizPassed = true;
        this.toastr.success('Поздравляю, вы успешно создали квиз','')
      });
    }
    else {      
      console.log('not valid');
      this.showErrors = true;
    }
  }
}
