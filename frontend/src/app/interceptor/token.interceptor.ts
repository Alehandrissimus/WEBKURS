import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { AuthService } from '../modules/auth/services/auth.service';
import { environment } from 'src/environments/environment';

@Injectable()
export class TokenInterceptor implements HttpInterceptor {

  constructor(private authService: AuthService) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {    
    req = req.clone({
      url: environment.API_URL + req.url,
      setHeaders: this.authService.isAuthenticated() ? {
        Authorization: `Bearer ${this.authService.getToken()}`
      } : {}
    });
    return next.handle(req).pipe(
      catchError(err => {
        if (err.status === 401 || err.status === 403) {
          // localStorage.clear();
          // sessionStorage.clear();
        }
        return throwError(err);
      }
      )
    );
  }
}
