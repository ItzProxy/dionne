import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {UserListComponent} from './users/user-list/user-list.component';
import {UserComponent} from './users/user/user.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HttpClientModule} from "@angular/common/http";
import {AddUserComponent } from './users/add-user/add-user.component';
import {NgbModalModule} from "@ng-bootstrap/ng-bootstrap";
import {UserEmailsComponent} from "./users/user-emails/user-emails.component";
import { AddEmailToUserComponent } from './users/add-email-to-user/add-email-to-user.component';
import {EmailModel} from "./models/email.model";
import {UserModel} from "./models/user.model";
import { RemoveEmailComponent } from './users/remove-email/remove-email.component';
import { HeaderComponent } from './header/header.component';
import { FooterComponent } from './footer/footer.component';

@NgModule({
  declarations: [
    AppComponent,
    UserListComponent,
    UserComponent,
    AddUserComponent,
    UserEmailsComponent,
    AddEmailToUserComponent,
    RemoveEmailComponent,
    HeaderComponent,
    FooterComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    NgbModalModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
