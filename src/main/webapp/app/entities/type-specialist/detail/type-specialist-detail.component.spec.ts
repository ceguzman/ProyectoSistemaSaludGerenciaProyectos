import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { TypeSpecialistDetailComponent } from './type-specialist-detail.component';

describe('TypeSpecialist Management Detail Component', () => {
  let comp: TypeSpecialistDetailComponent;
  let fixture: ComponentFixture<TypeSpecialistDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TypeSpecialistDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: TypeSpecialistDetailComponent,
              resolve: { typeSpecialist: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(TypeSpecialistDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TypeSpecialistDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load typeSpecialist on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TypeSpecialistDetailComponent);

      // THEN
      expect(instance.typeSpecialist()).toEqual(expect.objectContaining({ id: 123 }));
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
