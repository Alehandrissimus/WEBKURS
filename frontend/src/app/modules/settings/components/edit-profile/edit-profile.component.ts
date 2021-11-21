import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { User } from 'src/app/models/user';
import { UserService } from 'src/app/shared/services/users.service';

@Component({
  selector: 'app-edit-profile',
  templateUrl: './edit-profile.component.html',
  styleUrls: ['./edit-profile.component.scss']
})
export class EditProfileComponent implements OnInit {

  user: User;
  editForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private userService: UserService
  ) { }

  ngOnInit(): void {
    this.getUser(); 
    // подумай, как исправить
    this.editForm = this.fb.group({
      firstName:   ['', [Validators.required]], 
      lastName:    ['', [Validators.required]],
      email:       ['', [Validators.email, Validators.required]],
      description: ['', [Validators.required]]
    });   
    setTimeout(() => {
      this.editForm = this.fb.group({
        firstName:   [this.user?.firstName,   [Validators.required]], 
        lastName:    [this.user?.lastName,    [Validators.required]],
        email:       [this.user?.email,       [Validators.email, Validators.required]],
        description: [this.user?.description, [Validators.required]]
      });
    }, 500);
  }

  getUser() {
    this.userService.userInfo$.subscribe(user => this.user = user);
  }

  onSubmit() {
    // if(this.editForm.valid) {
    this.userService.editOne(this.user.id, this.editForm.value).subscribe(() => {
      this.userService.userInfo$.subscribe(user => this.user = user);
      this.userService.subject$.next(1);
    });
    // }
  }

}