import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { sha256 } from 'js-sha256';
import { AuthService } from '../../services/auth.service';
import { MustMatch } from '../../validators/must-match.validator';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {

  showPassword = false;
  showErrors = false;
  
  registerForm: FormGroup = this.fb.group({
    firstName:        ['', [Validators.required]],
    lastName:         ['', [Validators.required]],
    email:            ['', [Validators.email, Validators.required]],
    password:         ['', [Validators.minLength(5) ,Validators.required]],
    confirmPassword:  ['', [Validators.required]]
  }, {
    validator:  MustMatch('password', 'confirmPassword')
  });

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) { }

  ngOnInit(): void {        
    this.registerForm.valueChanges.subscribe(() => this.showErrors = false);
  }
  
  onSubmit(): void {
    if(this.registerForm.valid) {
      const data = {
        firstName:        this.registerForm.value.firstName,
        lastName:         this.registerForm.value.lastName,    
        email:            this.registerForm.value.email,    
        password:         sha256(this.registerForm.value.password),
        confirmPassword:  sha256(this.registerForm.value.confirmPassword)
      };            
      this.authService.register(data).subscribe(data => console.log(data));
      this.router.navigateByUrl('/auth/login');
    }
    else {
      this.showErrors = true;
      console.log('not valid');
    }
  }

}
