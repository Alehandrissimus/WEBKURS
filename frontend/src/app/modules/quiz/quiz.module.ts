import { NgModule } from '@angular/core';
import { CommonModule, registerLocaleData } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { QuizRoutingModule } from './quiz-routing.module';
import { QuizComponent } from './components/quiz/quiz.component';
import localeRu from '@angular/common/locales/ru';
import { CreateQuizTitleComponent } from './components/create-quiz-title/create-quiz-title.component';
import { QuizQuestionsComponent } from './components/quiz-questions/quiz-questions.component';
import { CategoriesBtnComponent } from 'src/app/shared/components/categories-btn/categories-btn.component';
import { QuizGameComponent } from './components/quiz-game/quiz-game.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

registerLocaleData(localeRu, 'ru');

@NgModule({
  declarations: [
    QuizComponent,
    CreateQuizTitleComponent,
    QuizQuestionsComponent,
    CategoriesBtnComponent,
    QuizGameComponent
    ],
  imports: [
    CommonModule,
    QuizRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    NgbModule
  ]
})
export class QuizModule { }
