import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { QuizType } from 'src/app/models/quiz';

@Component({
  selector: 'app-categories-btn',
  templateUrl: './categories-btn.component.html',
  styleUrls: ['./categories-btn.component.scss']
})
export class CategoriesBtnComponent implements OnInit {

  @Output() OnSelect: EventEmitter<string> = new EventEmitter();
  @Input() quizType: QuizType[] | any;
  
  categoriesControl: FormControl = new FormControl('0');

  constructor(
    private fb: FormBuilder
  ) { }

  ngOnInit(): void {
    this.categoriesControl.valueChanges.subscribe(val => this.OnSelect.emit(val));
  }

}
