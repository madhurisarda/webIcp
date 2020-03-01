import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-search-api',
  templateUrl: './search-api.component.html',
  styleUrls: ['./search-api.component.css']
})
export class SearchAPIComponent implements OnInit {

  @Input() results: any;
  constructor() { }

  ngOnInit() {
  }

}
