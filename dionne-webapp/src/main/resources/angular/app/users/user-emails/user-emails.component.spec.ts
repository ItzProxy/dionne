import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UserEmailsComponent } from './user-emails.component';

describe('UserEmailsComponent', () => {
  let component: UserEmailsComponent;
  let fixture: ComponentFixture<UserEmailsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UserEmailsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UserEmailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
