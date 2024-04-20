import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../medical-appointments.test-samples';

import { MedicalAppointmentsFormService } from './medical-appointments-form.service';

describe('MedicalAppointments Form Service', () => {
  let service: MedicalAppointmentsFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MedicalAppointmentsFormService);
  });

  describe('Service methods', () => {
    describe('createMedicalAppointmentsFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMedicalAppointmentsFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dateMedical: expect.any(Object),
            typeSpecialist: expect.any(Object),
          }),
        );
      });

      it('passing IMedicalAppointments should create a new form with FormGroup', () => {
        const formGroup = service.createMedicalAppointmentsFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dateMedical: expect.any(Object),
            typeSpecialist: expect.any(Object),
          }),
        );
      });
    });

    describe('getMedicalAppointments', () => {
      it('should return NewMedicalAppointments for default MedicalAppointments initial value', () => {
        const formGroup = service.createMedicalAppointmentsFormGroup(sampleWithNewData);

        const medicalAppointments = service.getMedicalAppointments(formGroup) as any;

        expect(medicalAppointments).toMatchObject(sampleWithNewData);
      });

      it('should return NewMedicalAppointments for empty MedicalAppointments initial value', () => {
        const formGroup = service.createMedicalAppointmentsFormGroup();

        const medicalAppointments = service.getMedicalAppointments(formGroup) as any;

        expect(medicalAppointments).toMatchObject({});
      });

      it('should return IMedicalAppointments', () => {
        const formGroup = service.createMedicalAppointmentsFormGroup(sampleWithRequiredData);

        const medicalAppointments = service.getMedicalAppointments(formGroup) as any;

        expect(medicalAppointments).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMedicalAppointments should not enable id FormControl', () => {
        const formGroup = service.createMedicalAppointmentsFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMedicalAppointments should disable id FormControl', () => {
        const formGroup = service.createMedicalAppointmentsFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
