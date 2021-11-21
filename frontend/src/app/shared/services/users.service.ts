import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { shareReplay, switchMap, tap } from 'rxjs/operators';
import { User } from 'src/app/models/user';
import { AbstractCrudService } from '../abstracts/abstract-crud.service';

@Injectable({
  providedIn: 'root'
})
export class UserService extends AbstractCrudService<User>{

  path = 'user';
  // userId: number;

  subject$ = new BehaviorSubject(1);
  userInfo$ = this.subject$.pipe(
    switchMap((val) => 
      val ? this.getMe().pipe(
        switchMap(({ id }) => this.getOne(id))
      ) : of(null)
    ),
    // tap(user => this.userId = user.id),
    shareReplay({refCount: true, bufferSize: 1})
  )

  constructor(protected http: HttpClient) {
    super(http);
  }

  getMe(): Observable<User> {
    return this.http.get<User>('user/me');
  }

}
