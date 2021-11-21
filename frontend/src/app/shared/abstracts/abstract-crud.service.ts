import { HttpClient} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export abstract class AbstractCrudService<T> {

  protected path!: string;

  constructor(protected http: HttpClient) { }

  getList(params: string = ''.toLowerCase()): Observable<T[]> {
    return this.http.get<T[]>(this.path + params);
  }

  getOne(id: number, params: string = ''): Observable<T> {
    return this.http.get<T>(this.path + '/' + id + params);
  }

  createOne(item: Partial<T>, params: string = ''): Observable<T> {
    return this.http.post<T>(this.path + params, item);
  }

  editOne(id: number, item: Partial<T>, params: string = ''): Observable<T> {
    return this.http.put<T>(this.path + '/' + id + params, item);
  }

  deleteOne(id: number, params: string = '') {
    return this.http.delete(this.path + '/' + id + params);
  }
}
