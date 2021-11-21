import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { QuizType } from 'src/app/models/quiz';
import { QuizGuard } from '../../quiz.guard';

@Component({
  selector: 'app-create-quiz-title',
  templateUrl: './create-quiz-title.component.html',
  styleUrls: ['./create-quiz-title.component.scss']
})
export class CreateQuizTitleComponent implements OnInit {

  quizTypes = QuizType;
  showErrors = false;

  createQuizForm: FormGroup = this.fb.group({
    title:       ['', [Validators.required]],
    quizType:    ['GEOGRAPHICAL', [Validators.required]],
    description: ['', [Validators.required]]
  });

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private quizGuard: QuizGuard
  ) { }

  ngOnInit(): void {    
    this.createQuizForm.valueChanges.subscribe(() => this.showErrors = false);
  }

  onSubmit(): void {
    if(this.createQuizForm.valid) {
      this.quizGuard.data = true;
      this.router.navigate(['/quiz/quiz-questions', {data: JSON.stringify(this.createQuizForm.value)}])
    }
    else {
      this.showErrors = true;
      console.log('not valid');
    }
  }

}
