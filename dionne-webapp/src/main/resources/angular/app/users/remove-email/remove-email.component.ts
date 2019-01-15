import {Component, Input, OnInit} from '@angular/core';
import {NgbModal, ModalDismissReasons, NgbActiveModal} from "@ng-bootstrap/ng-bootstrap"

@Component({
  selector: 'app-remove-email',
  templateUrl: './remove-email.component.html',
  styleUrls: ['./remove-email.component.scss']
})
export class RemoveEmailComponent {

  @Input('emailId')
    emailId : string;

  constructor(private modalService: NgbModal) {}

  deleteEmail(){
    console.log("Will remove: ${emailId}")
  }

}
