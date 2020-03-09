import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import { SearchAPIComponent} from '../search-api/search-api.component';
import { ControlMessagesComponent} from '../control-messages/control-messages.component';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  public results: any;
  searchAPIForm: FormGroup;


  constructor( private http: HttpClient, private fb: FormBuilder) { }



  ngOnInit() {
    this.searchAPIForm = this.fb.group({
      search: ['Taylor Swift', [Validators.required]]
    });

    this.getSearchResults();
  }

  getSearchResults(): void {
    this.http.get('https://kgsearch.googleapis.com/v1/entities:search?query=' + this.searchAPIForm.controls.search.value + '&key=AIzaSyAKgxm1Or1uLqMIAK5P2MXUUA5fj7I9QNY').subscribe(data => {
      this.results = data;
    });

  }
}
