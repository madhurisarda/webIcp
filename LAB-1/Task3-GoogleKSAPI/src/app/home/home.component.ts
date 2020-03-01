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

  public searchResults: any;
  searchAPIForm: FormGroup;


  constructor( private http: HttpClient, private fb: FormBuilder) { }



  ngOnInit() {
    this.searchAPIForm = this.fb.group({
      search: ['Taylor Swift', [Validators.required]]
    });

    this.getSearchResults();
  }

  getSearchResults(): void {
    this.http.get('https://kgsearch.googleapis.com/v1/entities:search?query=' + this.searchAPIForm.controls.search.value + '&key=fceebdff8af0e89319ba4a255f7ee452').subscribe(data => {
      this.searchResults = data;
      console.log(data);
    });

    // responsiveVoice.speak(this.searchRecipeForm.controls.recipeName.value );
  }
}
