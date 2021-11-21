import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { switchMap, tap } from 'rxjs/operators';
import { Dashboard } from 'src/app/models/dashboard';
import { User } from 'src/app/models/user';
import { UserService } from 'src/app/shared/services/users.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {

  user: User;
  dashboard: Dashboard;

  constructor(
    private userService: UserService,
    private http: HttpClient
  ) { }

  ngOnInit(): void {
    this.getDashboard();
  }

  getDashboard(): void {
    this.userService.userInfo$.pipe(
      tap(user => this.user = user),
      switchMap(({id}) => this.http.get<Dashboard>(`dashboard/${id}`))
    ).subscribe(dashboard => this.dashboard = dashboard);
  }

  onLike(annoucement) {
    this.http.post('announcement/like', {idAnnouncement: annoucement.id}).subscribe(() => this.getDashboard());
  }

}
