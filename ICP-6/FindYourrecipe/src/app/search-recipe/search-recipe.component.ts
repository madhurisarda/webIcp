import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Component({
  selector: 'app-search-recipe',
  templateUrl: './search-recipe.component.html',
  styleUrls: ['./search-recipe.component.css']
})
export class SearchRecipeComponent implements OnInit {
  @ViewChild('recipe') recipes: ElementRef;
  @ViewChild('place') places: ElementRef;
  recipeValue: any;
  placeValue: any;
  venueList = [];
  recipeList = [];
  formattedAddress = [];

  currentLat: any;
  currentLong: any;
  geolocationPosition: any;

  recepieApi = 'https://api.edamam.com/search?q=';
  recepieAppid = '&app_id=454fe335';
  recepieKey = '&app_key=0773cc089b069e8196a3ad56c6374a42';

  placesApi = 'https://api.foursquare.com/v2/venues/search?';
  clientdId = 'client_id=AZ5YEHLFZWTZJQAO0YPRGLGIIVFQOS3MRSPFHDOZRQ1XU1PD';
  clientSecret = '&client_secret=1HKYC2PGOKIAYIQAXDCGZOSAADRLRVND0UMF5VLRA1GVDJ4A';
  version = '&v=20180323';
  near1 = '&near=';
  query = '&query=';
  constructor(private _http: HttpClient) {
  }

  ngOnInit() {

    window.navigator.geolocation.getCurrentPosition(
      position => {
        this.geolocationPosition = position;
        this.currentLat = position.coords.latitude;
        this.currentLong = position.coords.longitude;
      });
  }

  getVenues() {

    this.recipeValue = this.recipes.nativeElement.value;
    this.placeValue = this.places.nativeElement.value;

    if (this.recipeValue !== null) {
      this._http.get( this.recepieApi + this.recipeValue + this.recepieAppid + this.recepieKey).subscribe((result: any) => {
        console.log(result);
        this.recipeList = Object.keys(result.hits).map(function (k) {
          const i = result.hits[k].recipe;
          return {name: i.label, icon: i.image, url: i.url};
        });

      });

    }

    if (this.placeValue != null && this.placeValue !== '' && this.recipeValue != null && this.recipeValue !== '') {
      this._http.get( this.placesApi  + this.clientdId + this.clientSecret + this.version
        + this.near1 + this.placeValue).subscribe((result: any) => {
        console.log(result);
        this.venueList = Object.keys(result.response.venues).map(function (k) {
          const i = result.response.venues[k];
          return {name: i.name, currentLat: '39.0349657', currentLong: '-94.5787524', formattedAddress: i.location.formattedAddress};
        });
      });
    }
  }
}
