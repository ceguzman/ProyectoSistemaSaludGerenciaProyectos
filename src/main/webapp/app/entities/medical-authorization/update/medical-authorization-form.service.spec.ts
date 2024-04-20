import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../medical-authorization.test-samples';

import { MedicalAuthorizationFormService } from './medical-authorization-form.service';

describe('MedicalAuthorization Form Service', () => {
  let service: MedicalAuthorizationFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MedicalAuthorizationFormService);
  });

  describe('Service methods', () => {
    describe('createMedicalAuthorizationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMedicalAuthorizationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            detailAuthorization: expect.any(Object),
            stateAuthorization: expect.any(Object),
            dateAuthorization: expect.any(Object),
            clinicHistory: expect.any(Object),
          }),
        );
      });

      it('passing IMedicalAuthorization should create a new form with FormGroup', () => {
        const formGroup = service.createMedicalAuthorizationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            detailAuthorization: expect.any(Object),
            stateAuthorization: expect.any(Object),
            dateAuthorization: expect.any(Object),
            clinicHistory: expect.any(Object),
          }),
        );
      });
    });

    describe('getMedicalAuthorization', () => {
      it('should return NewMedicalAuthorization for default MedicalAuthorization initial value', () => {
        const formGroup = service.createMedicalAuthorizationFormGroup(sampleWithNewData);

        const medicalAuthorization = service.getMedicalAuthorization(formGroup) as any;

        expect(medicalAuthorization).toMatchObject(sampleWithNewData);
      });

      it('should return NewMedicalAuthorization for empty MedicalAuthorization initial value', () => {
        const formGroup = service.createMedicalAuthorizationFormGroup();

        const medicalAuthorization = service.getMedicalAuthorization(formGroup) as any;

        expect(medicalAuthorization).toMatchObject({});
      });

      it('should return IMedicalAuthorization', () => {
        const formGroup = service.createMedicalAuthorizationFormGroup(sampleWithRequiredData);

        const medicalAuthorization = service.getMedicalAuthorization(formGroup) as any;

        expect(medicalAuthorization).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMedicalAuthorization should not enable id FormControl', () => {
        const formGroup = service.createMedicalAuthorizationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMedicalAuthorization should disable id FormControl', () => {
        const formGroup = service.createMedicalAuthorizationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
