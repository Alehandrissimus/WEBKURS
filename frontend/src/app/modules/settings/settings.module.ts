import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SettingsRoutingModule } from './settings-routing.module';
import { EditProfileComponent } from './components/edit-profile/edit-profile.component';
import { ChangePasswordComponent } from './components/change-password/change-password.component';
import { SelectionMenuComponent } from './components/selection-menu/selection-menu.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

// ChangePasswordComponent
@NgModule({
  declarations: [EditProfileComponent, ChangePasswordComponent, SelectionMenuComponent],
  imports: [
    CommonModule,
    SettingsRoutingModule,
    FormsModule,
    ReactiveFormsModule
  ],
  providers: [NgbActiveModal]
})
export class SettingsModule { }
