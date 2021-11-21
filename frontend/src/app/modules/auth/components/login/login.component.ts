import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { sha256 } from 'js-sha256';
import { UserService } from 'src/app/shared/services/users.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  showPassword = false;
  showErrors = false;
  userId: number;

  loginForm: FormGroup = this.fb.group({
    email:    ['', [Validators.email, Validators.required]],
    password: ['', [Validators.minLength(8), Validators.required]],
    remember: [true]
  });

  constructor(
    private router: Router,
    private fb: FormBuilder,
    private authService: AuthService,
    private userService: UserService
    ) { }

  ngOnInit(): void {
    this.getUser();
    this.loginForm.valueChanges.subscribe(() => this.showErrors = false);
  }

  getUser(): void {
    this.userService.userInfo$.subscribe(({id}) => this.userId = id);
  }

  onSubmit(): void {
    if(this.loginForm.valid) {
      const data = {
        email:    this.loginForm.value.email,
        password: sha256(this.loginForm.value.password),
        remember: this.loginForm.value.remember
      };
      this.authService.login(data, data.remember).subscribe((userData) => {
        this.userId = userData.user.id;
        this.router.navigateByUrl('/profile')
      },err => {
        this.showErrors = true;
      });
    }
    else {
      this.showErrors = true;
    }

  }

}
