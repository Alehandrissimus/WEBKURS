import { NgModule } from '@angular/core';
import { CommonModule, registerLocaleData } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AnnouncmentRoutingModule } from './announcment-routing.module';
import { AnnouncmentComponent } from './components/announcment.component';
import { CreateAnnouncementComponent } from 'src/app/modals/create-announcement/components/create-announcement';
import localeRu from '@angular/common/locales/ru';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

registerLocaleData(localeRu, 'ru');

@NgModule({
  declarations: [AnnouncmentComponent,CreateAnnouncementComponent],
  imports: [
    CommonModule,
    AnnouncmentRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    NgbModule
  ]
})
export class AnnouncmentModule { }
