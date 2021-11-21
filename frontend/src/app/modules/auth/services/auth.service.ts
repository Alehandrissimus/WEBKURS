import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AuthResponse } from 'src/app/models/auth/auth';
import { LoginRequest } from 'src/app/models/auth/login';
import { tap } from 'rxjs/operators';
import { User } from 'src/app/models/user';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  path?: ''

  constructor(private http: HttpClient) { }

  login(data: LoginRequest, remember: boolean): Observable<AuthResponse> {
    return this.http.post<AuthResponse>('auth/local', data).pipe(
      tap(authResponse => this.setTokens(authResponse, remember))
    );
  }
  // поменяй any!
  register(data: any): Observable<User> {
    return this.http.post<User>('auth/local/register', data)
  }

  forgotPassword(email: string): Observable<string> {
    return this.http.post<string>('auth/recover', email);
  }
  
  setTokens(authResponse: AuthResponse, remember: boolean): void {
    if(remember) {
      localStorage.setItem('access_token', authResponse.jwt);
    }
    else {
      sessionStorage.setItem('access_token', authResponse.jwt)
    }
  }

  isAuthenticated(): boolean {
    return Boolean(localStorage.getItem('access_token') || Boolean(sessionStorage.getItem('access_token')));
  }

  getToken(): string | null {
    return localStorage.getItem('access_token') ?? sessionStorage.getItem('access_token');
  }
  
}
