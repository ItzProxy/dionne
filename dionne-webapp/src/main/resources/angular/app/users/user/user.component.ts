import {Component, OnChanges} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {UserService} from "../../services/user.service";
import {ActivatedRoute} from "@angular/router";
import {UserModel} from "../../models/user.model";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss']
})
export class UserComponent {

  formGroup: FormGroup = this.createFormGroup();
  userId : string;
  savingSubscription = Subscription.EMPTY;

  constructor(private formBuilder: FormBuilder,
              private userService: UserService,
              private activatedRoute: ActivatedRoute) {

    this.activatedRoute.params.subscribe(params => {
      this.userId = params['userId'];
      this.userService.loadCurrentUserByUserId(this.userId);
    });

    this.userService.currentUser$.subscribe(user => {
      if (user) {
        this.formGroup.patchValue(user);
      }
    });
  }

  saveForm(): void {
    const userToSave = this.formGroup.getRawValue() as UserModel;
    this.savingSubscription = this.userService.saveUser(userToSave,
        user => console.log('We saved the user!'));
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
