import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {UserListComponent} from "./users/user-list/user-list.component";
import {UserComponent} from "./users/user/user.component";
import {AddUserComponent} from "./users/add-user/add-user.component";

const routes: Routes = [
  {
    path: '',
    component: UserListComponent,
  },
  {
    path: ':userId',
    component: UserComponent,
  },
  {
    path: '',
    component: AddUserComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
