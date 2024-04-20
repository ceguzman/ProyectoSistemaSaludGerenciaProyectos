import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { MedicalAppointmentsDetailComponent } from './medical-appointments-detail.component';

describe('MedicalAppointments Management Detail Component', () => {
  let comp: MedicalAppointmentsDetailComponent;
  let fixture: ComponentFixture<MedicalAppointmentsDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MedicalAppointmentsDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: MedicalAppointmentsDetailComponent,
              resolve: { medicalAppointments: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(MedicalAppointmentsDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MedicalAppointmentsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load medicalAppointments on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', MedicalAppointmentsDetailComponent);

      // THEN
      expect(instance.medicalAppointments()).toEqual(expect.objectContaining({ id: 123 }));
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
