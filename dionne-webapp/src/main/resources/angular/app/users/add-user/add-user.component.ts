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

  private closeResult: string;
  formGroup: FormGroup = this.createFormGroup();
  constructor(private formBuilder: FormBuilder,
              private userService: UserService,
              private modalService: NgbModal){ }

  ngOnInit() {
  }
  open(content) {
    this.modalService.open(content, {ariaLabelledBy: 'modal-basic-title'}).result.then((result) => {
      this.closeResult = `Closed with: ${result}`;
    }, (reason) => {
      this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
    });
  }

  private getDismissReason(reason: any): string {
    if (reason === ModalDismissReasons.ESC) {
      return 'by pressing ESC';
    } else if (reason === ModalDismissReasons.BACKDROP_CLICK) {
      return 'by clicking on a backdrop';
    } else {
      return  `with: ${reason}`;
    }
  }

  saveForm(): void {
    const userToSave = this.formGroup.getRawValue() as UserModel;
    console.log(this.formGroup.getRawValue());
    console.log(this.userService.createNewUser(userToSave).subscribe());
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
