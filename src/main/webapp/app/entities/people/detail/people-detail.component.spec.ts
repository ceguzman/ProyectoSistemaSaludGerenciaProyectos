import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { PeopleDetailComponent } from './people-detail.component';

describe('People Management Detail Component', () => {
  let comp: PeopleDetailComponent;
  let fixture: ComponentFixture<PeopleDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PeopleDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: PeopleDetailComponent,
              resolve: { people: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(PeopleDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PeopleDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load people on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', PeopleDetailComponent);

      // THEN
      expect(instance.people()).toEqual(expect.objectContaining({ id: 123 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
