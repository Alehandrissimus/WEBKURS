import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LayoutComponent } from './layout/layout.component';
import { AuthGuard } from './modules/auth/auth.guard';
import { QuizGameComponent } from './modules/quiz/components/quiz-game/quiz-game.component';
import { QuizQuestionsComponent } from './modules/quiz/components/quiz-questions/quiz-questions.component';
import { QuizGuard } from './modules/quiz/quiz.guard';

const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: '/auth/login' },
  {
    path: '', 
    component: LayoutComponent,
    children: [
      { path: '', pathMatch: 'full', redirectTo: 'test' },
      { path: 'profile', loadChildren: () => import('./modules/profile/profile/profile.module').then(m => m.ProfileModule) },
      { path: 'test', loadChildren: () => import('./modules/test/test.module').then(m => m.TestModule) },
      { path: 'dashboard', loadChildren: () => import('./modules/dashboard/dashboard.module').then(m => m.DashboardModule) },
      { path: 'announcement', loadChildren: () => import('./modules/announcement/announcment.module').then(m => m.AnnouncmentModule) },
      { path: 'settings', loadChildren: () => import('./modules/settings/settings.module').then(m => m.SettingsModule) },
      { path: 'quiz', loadChildren: () => import('./modules/quiz/quiz.module').then(m => m.QuizModule) }
    ],
    canActivate: [AuthGuard]
  },
  {
    path: 'quiz/quiz-questions', component: QuizQuestionsComponent, canActivate: [QuizGuard]
  },
  {
    path: 'quiz/:id', component: QuizGameComponent 
  },
  {
    path: 'auth', loadChildren: () => import('./modules/auth/auth.module').then(m => m.AuthModule)
  }
  // { path: '**', component: NotFoundComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
