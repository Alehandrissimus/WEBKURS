import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AnnouncmentComponent } from './components/announcment.component';

const routes: Routes = [
  { path: '', component: AnnouncmentComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AnnouncmentRoutingModule { }
