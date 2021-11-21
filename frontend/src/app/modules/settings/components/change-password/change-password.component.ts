import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { sha256 } from 'js-sha256';
import { User } from 'src/app/models/user';
import { RecoveryComponent } from 'src/app/modules/auth/components/recovery/recovery.component';
import { MustMatch } from 'src/app/modules/auth/validators/must-match.validator';
import { UserService } from 'src/app/shared/services/users.service';

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.scss']
})

export class ChangePasswordComponent implements OnInit {

  user: User;
  changeForm = this.fb.group({
    oldPass:      ['', [Validators.required]], 
    newPass:      ['', [Validators.required]],
    confirmPass:  ['', [Validators.required]],
  }, {
    validator: MustMatch("newPass", "confirmPass")
  });

  constructor(
    private userService: UserService,
    private fb: FormBuilder,
    private modal: NgbModal,
    private http: HttpClient
  ) { }

  ngOnInit(): void {
    this.getUser();
  }

  getUser(): void {
    this.userService.userInfo$.subscribe(user => this.user = user);
  }

  openRecovery() {
    let modalRef = this.modal.open(RecoveryComponent, { centered: true });
    modalRef.componentInstance.isModal = true;
  }

  onSubmit() {
    if(this.changeForm.valid) {
      const data = {
        oldPass: sha256(this.changeForm.value.oldPass),
        newPass: sha256(this.changeForm.value.newPass),
        confirmPass: sha256(this.changeForm.value.confirmPass)
      }      
      this.http.put(`updatePassword/${this.user.id}`, data).subscribe(() => console.log());
    }
  }
}
