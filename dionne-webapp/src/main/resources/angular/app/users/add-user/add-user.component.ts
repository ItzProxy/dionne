import { Component, OnInit } from '@angular/core';
import {UserModel} from "../../models/user.model";
import {FormBuilder, FormGroup} from "@angular/forms";
import {UserService} from "../../services/user.service";
import {NgbModal, ModalDismissReasons} from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-add-user',
  templateUrl: './add-user.component.html',
  styleUrls: ['./add-user.component.scss']
})
export class AddUserComponent implements OnInit {
  formGroup: FormGroup = this.createFormGroup();
  constructor(private formBuilder: FormBuilder,
              private userService: UserService,
              private modalService: NgbModal){ }

  ngOnInit() {
  }
  open(content) {
    this.modalService.open(content, {ariaLabelledBy: 'modal-basic-title'});
  }

  saveForm(): void {
    const userToSave = this.formGroup.getRawValue() as UserModel;
    this.userService.saveUser(userToSave);
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
