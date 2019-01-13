import {Component, OnChanges} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {UserService} from "../../services/user.service";
import {ActivatedRoute} from "@angular/router";
import {UserModel} from "../../models/user.model";

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss']
})
export class UserComponent {

  formGroup: FormGroup = this.createFormGroup();
  saved : Boolean = false;


  constructor(private formBuilder: FormBuilder,
              private userService: UserService,
              private activatedRoute: ActivatedRoute) {

    this.activatedRoute.params.subscribe(params => {
      const userId = params['userId'];
      this.userService.loadCurrentUserByUserId(userId);
    });

    this.userService.currentUser$.subscribe(user => {
      if (user) {
        this.formGroup.patchValue(user);
      }
    });

    this.userService.sav
  }

  saveForm(): void {
    const userToSave = this.formGroup.getRawValue() as UserModel;
    console.log(this.userService.saveCurrentUser(userToSave));
  }

  private createFormGroup(): FormGroup {
    return this.formBuilder.group({
      "firstName": '',
      "lastName": '',
      "userId": '',
      "username" : ''
    });
  }

}
