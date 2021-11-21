import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthService } from '../auth/services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class QuizGuard implements CanActivate {

  data = false;

  constructor(private authService: AuthService, private router: Router) {}

  canActivate(): boolean {
    // if(!this.authService.isAuthenticated) {
    //   this.router.navigateByUrl('/auth/login');
    //   return false;
    // }
    // if (!this.data) {
    //   this.router.navigateByUrl('/quiz');
    //   return false;
    // }
    return true;
  }


}
