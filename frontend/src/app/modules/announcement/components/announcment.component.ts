import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { debounceTime, switchMap, tap } from 'rxjs/operators';
import { CreateAnnouncementComponent } from 'src/app/modals/create-announcement/components/create-announcement';
import { Announcement } from 'src/app/models/announcements';

@Component({
  selector: 'app-announcment',
  templateUrl: './announcment.component.html',
  styleUrls: ['./announcment.component.scss']
})
export class AnnouncmentComponent implements OnInit {

  announcements: Announcement;
  searchProject: FormControl = new FormControl('');
  page = 1;
  totalElements = 8;
  
  constructor(
    private modalService: NgbModal,
    private http: HttpClient
  ) { }

  ngOnInit(): void {    
    this.getAnnouncement();
    this.retrieveFilterChanges();    
  }

  getAnnouncement() {
    this.http.get<Announcement>(`announcement/search?page=${this.page}&title=${this.searchProject.value}`).subscribe((announcement: any) => {
      this.announcements = announcement.content
      this.totalElements = announcement.totalElements
    });
  }

  retrieveFilterChanges() {
    this.searchProject.valueChanges.pipe(
      debounceTime(300),
      tap(() => this.page=1)).subscribe(() => this.getAnnouncement());
  }

  onLike(annoucement) {
    this.http.post('announcement/like', {idAnnouncement: annoucement.id}).subscribe(() => this.getAnnouncement());
  }

  createAnnouncement(): void {
    const modalRef = this.modalService.open(CreateAnnouncementComponent, { centered: true });
    modalRef.closed.pipe(
      switchMap(announcement => this.http.post<Announcement>('announcement/create', announcement))
    ).subscribe(() =>
      this.getAnnouncement()
    );
  }
}
