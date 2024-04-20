import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { MedicalProceduresDetailComponent } from './medical-procedures-detail.component';

describe('MedicalProcedures Management Detail Component', () => {
  let comp: MedicalProceduresDetailComponent;
  let fixture: ComponentFixture<MedicalProceduresDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MedicalProceduresDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: MedicalProceduresDetailComponent,
              resolve: { medicalProcedures: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(MedicalProceduresDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MedicalProceduresDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load medicalProcedures on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', MedicalProceduresDetailComponent);

      // THEN
      expect(instance.medicalProcedures()).toEqual(expect.objectContaining({ id: 123 }));
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
