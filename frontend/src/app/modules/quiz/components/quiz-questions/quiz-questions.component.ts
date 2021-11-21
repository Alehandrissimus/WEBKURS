import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { QuestionType, Quiz } from 'src/app/models/quiz';

@Component({
  selector: 'app-quiz-questions',
  templateUrl: './quiz-questions.component.html',
  styleUrls: ['./quiz-questions.component.scss']
})
export class QuizQuestionsComponent implements OnInit {

  data: Quiz; 
  questionTypes = QuestionType;
  currentQuiz: number = 0;
  quizId: number = 0;
  showErrors = false;

  questionsForm: FormGroup = this.fb.group({
    questions: this.fb.array([
      this.newQuestionForm(),
      this.newQuestionForm(),
      this.newQuestionForm(),
      this.newQuestionForm(),
      this.newQuestionForm(),
      this.newQuestionForm(),
      this.newQuestionForm(),
      this.newQuestionForm(),
      this.newQuestionForm(),
      this.newQuestionForm()
    ])
  });

  constructor(
    private route: ActivatedRoute,
    private fb: FormBuilder,
    private http: HttpClient,
    private toastr: ToastrService
  ) { }

  ngOnInit(): void {
    this.questionsForm.valueChanges.subscribe(() => this.showErrors = false);
    this.route.paramMap.subscribe((data: any) => this.data = JSON.parse(data.getAll('data')));
  }

  get questionArray(): FormArray {
    return this.questionsForm.get("questions") as FormArray;
  }

  get answerArray(): FormArray {
    return this.questionsForm.get("questions")['controls'][1].get("answers");
  }

  newQuestionForm(): FormGroup {
    this.quizId++;
    return this.fb.group({
      question:     ['', [Validators.required]],  // question title
      questionType: ['FOUR_ANSWERS', [Validators.required]],  // four answer
      answers:      this.fb.array([this.newAnswerForm(), this.newAnswerForm(), this.newAnswerForm(), this.newAnswerForm()])
    });
  }

  newAnswerForm(): FormGroup {
    return this.fb.group({
      value:  ['', [Validators.required]],  // текст ответа
      answer: ['FALSE', [Validators.required]]
    });
  }

  getQuestions(form) {
    return form.controls.questions.controls;
  }

  getAnswers(form) {
    return form.controls.answers.controls;
  }

  addAnswers(j) {
    const control = <FormArray>this.questionsForm.get('questions')['controls'][j].get("answers");
    control.push(this.newAnswerForm());
  }

  removeAnswers(j) {
    const control = <FormArray>this.questionsForm.get('questions')['controls'][j].get("answers");
    control.removeAt(0);
 }

  previous(): void {
    if (this.currentQuiz >= 1) { this.currentQuiz--; }
  }

  next(): void {
    if (this.currentQuiz <= 8) { this.currentQuiz++; }
  }

  logOut() {
    localStorage.removeItem('access_token');
    sessionStorage.removeItem('access_token');
  }

  isChecked(allAnswers, currentAnswer): void {
    allAnswers.map(v => v.answer = 'FALSE');
    currentAnswer.answer = 'TRUE';
  }

  onChangeType(quiz, index): void {
    if(quiz.questionType === "TRUE_FALSE") {
      this.removeAnswers(index);
      this.removeAnswers(index);          
    }
    else if(quiz.questionType === "FOUR_ANSWERS") {
      this.addAnswers(index);
      this.addAnswers(index);      
    }
  }

  onSubmit(): void {
    if(this.questionsForm.valid) {
    let data = Object.assign(this.data, this.questionsForm.value);
    console.log(data);
    this.http.post('quiz/', data).subscribe(() => this.toastr.success('Поздравляю, вы успешно создали квиз',''));
    }
    else {
    this.showErrors = true;      
    }
  }
}

