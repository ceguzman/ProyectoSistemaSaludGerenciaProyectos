import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../medical-procedures.test-samples';

import { MedicalProceduresFormService } from './medical-procedures-form.service';

describe('MedicalProcedures Form Service', () => {
  let service: MedicalProceduresFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MedicalProceduresFormService);
  });

  describe('Service methods', () => {
    describe('createMedicalProceduresFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMedicalProceduresFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            typeProcedures: expect.any(Object),
            description: expect.any(Object),
            dateProcedures: expect.any(Object),
            medicalAuthorization: expect.any(Object),
          }),
        );
      });

      it('passing IMedicalProcedures should create a new form with FormGroup', () => {
        const formGroup = service.createMedicalProceduresFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            typeProcedures: expect.any(Object),
            description: expect.any(Object),
            dateProcedures: expect.any(Object),
            medicalAuthorization: expect.any(Object),
          }),
        );
      });
    });

    describe('getMedicalProcedures', () => {
      it('should return NewMedicalProcedures for default MedicalProcedures initial value', () => {
        const formGroup = service.createMedicalProceduresFormGroup(sampleWithNewData);

        const medicalProcedures = service.getMedicalProcedures(formGroup) as any;

        expect(medicalProcedures).toMatchObject(sampleWithNewData);
      });

      it('should return NewMedicalProcedures for empty MedicalProcedures initial value', () => {
        const formGroup = service.createMedicalProceduresFormGroup();

        const medicalProcedures = service.getMedicalProcedures(formGroup) as any;

        expect(medicalProcedures).toMatchObject({});
      });

      it('should return IMedicalProcedures', () => {
        const formGroup = service.createMedicalProceduresFormGroup(sampleWithRequiredData);

        const medicalProcedures = service.getMedicalProcedures(formGroup) as any;

        expect(medicalProcedures).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMedicalProcedures should not enable id FormControl', () => {
        const formGroup = service.createMedicalProceduresFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMedicalProcedures should disable id FormControl', () => {
        const formGroup = service.createMedicalProceduresFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
