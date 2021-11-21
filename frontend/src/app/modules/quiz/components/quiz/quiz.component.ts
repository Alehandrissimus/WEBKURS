import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { Router } from '@angular/router';
import { debounceTime, switchMap, tap } from 'rxjs/operators';
import { Quiz, QuizType } from 'src/app/models/quiz';

@Component({
  selector: 'app-quiz',
  templateUrl: './quiz.component.html',
  styleUrls: ['./quiz.component.scss']
})
export class QuizComponent implements OnInit {

  quizzes: Quiz[];
  searchProject: FormControl = new FormControl('');
  quizType = QuizType;
  page = 1;
  totalElements = 6;

  constructor(
    private router: Router,
    private http: HttpClient
  ) { }

  ngOnInit(): void {    
    this.getQuizzes();
    this.retrieveFilterChanges();
  }

  onSelect(type: string) {
    this.http.get(`quiz/filter?filter=SCIENCE`).subscribe((v) => console.log(v))
  }

  retrieveFilterChanges() {
    this.searchProject.valueChanges.pipe(
      debounceTime(300),
      tap(() => this.page=1)).subscribe(() => this.getQuizzes());
  }

  getQuizzes() {
    this.http.get(`quiz/search?page=${this.page}&title=${this.searchProject.value}`).subscribe((quiz: any) =>  {
      this.quizzes = quiz.content;      
      this.totalElements = quiz.totalElements;
    });
  }
  
  createQuiz(): void {
    this.router.navigateByUrl('quiz/create-quiz');
  }

}
