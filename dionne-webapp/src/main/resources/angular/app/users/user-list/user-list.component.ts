import {Component, OnInit} from '@angular/core';
import {UserService} from "../../services/user.service";
import {UserModel} from "../../models/user.model";
import {Router} from "@angular/router";
import {Observable} from "rxjs";

@Component({
    selector: 'app-user-list',
    templateUrl: './user-list.component.html',
    styleUrls: ['./user-list.component.scss']
})
export class UserListComponent {
    users$ = this.userService.allUsers$;

    constructor(private userService: UserService,
                private router: Router) {
        this.userService.loadAllUsers();
    }



    navigateToUserDetails(user: UserModel) {
        this.router.navigateByUrl(user.userId);
    }
}