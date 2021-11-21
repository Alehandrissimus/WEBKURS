import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-recovery',
  templateUrl: './recovery.component.html',
  styleUrls: ['./recovery.component.scss']
})
export class RecoveryComponent implements OnInit {

  @Input() isModal = false;
  showErrors = false;
  recoveryForm: FormGroup = this.fb.group({
    email:  ['', [Validators.email, Validators.required]],
  });

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private toastr: ToastrService,
    private activeModal: NgbActiveModal
  ) { }

  ngOnInit(): void {
    this.recoveryForm.valueChanges.subscribe(() => this.showErrors = false);
  }

  openLogin(): void {
    this.activeModal.close();
    this.router.navigateByUrl('/auth/login')
  }

  onSubmit(): void {
    console.log(this.recoveryForm);
    
    if(this.recoveryForm.valid) {
      this.toastr.info(`Мы отправили ссылку для восстановления доступа к вашему аккаунту на адрес ${this.recoveryForm.value.email}.`, '', {
        timeOut: 2000,
      });    
      if(!this.isModal) {
        this.router.navigateByUrl('/auth/login')
      }
      this.authService.forgotPassword(this.recoveryForm.value).subscribe();
    }
    else {
      this.showErrors = true;
    }
  }

}
