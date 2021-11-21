import { Component, Input, OnInit } from '@angular/core';
import { Announcement } from 'src/app/models/announcements';

@Component({
  selector: 'app-announcement-card',
  templateUrl: './announcement-card.component.html',
  styleUrls: ['./announcement-card.component.scss']
})
export class AnnouncementCardComponent implements OnInit {
  
  @Input() announcements: Announcement;
  @Input() term: any;
  @Input() p: number;
  constructor(
  ) { }

  ngOnInit(): void {
  }

}
