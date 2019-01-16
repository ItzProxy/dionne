import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MakeEmailPrimaryComponent } from './make-email-primary.component';

describe('MakeEmailPrimaryComponent', () => {
  let component: MakeEmailPrimaryComponent;
  let fixture: ComponentFixture<MakeEmailPrimaryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MakeEmailPrimaryComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MakeEmailPrimaryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
