import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RemoveEmailComponent } from './remove-email.component';

describe('RemoveEmailComponent', () => {
  let component: RemoveEmailComponent;
  let fixture: ComponentFixture<RemoveEmailComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RemoveEmailComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RemoveEmailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
