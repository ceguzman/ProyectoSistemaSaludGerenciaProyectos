import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../medication-request.test-samples';

import { MedicationRequestFormService } from './medication-request-form.service';

describe('MedicationRequest Form Service', () => {
  let service: MedicationRequestFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MedicationRequestFormService);
  });

  describe('Service methods', () => {
    describe('createMedicationRequestFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMedicationRequestFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            amount: expect.any(Object),
            milligrams: expect.any(Object),
            medicalAuthorization: expect.any(Object),
          }),
        );
      });

      it('passing IMedicationRequest should create a new form with FormGroup', () => {
        const formGroup = service.createMedicationRequestFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            amount: expect.any(Object),
            milligrams: expect.any(Object),
            medicalAuthorization: expect.any(Object),
          }),
        );
      });
    });

    describe('getMedicationRequest', () => {
      it('should return NewMedicationRequest for default MedicationRequest initial value', () => {
        const formGroup = service.createMedicationRequestFormGroup(sampleWithNewData);

        const medicationRequest = service.getMedicationRequest(formGroup) as any;

        expect(medicationRequest).toMatchObject(sampleWithNewData);
      });

      it('should return NewMedicationRequest for empty MedicationRequest initial value', () => {
        const formGroup = service.createMedicationRequestFormGroup();

        const medicationRequest = service.getMedicationRequest(formGroup) as any;

        expect(medicationRequest).toMatchObject({});
      });

      it('should return IMedicationRequest', () => {
        const formGroup = service.createMedicationRequestFormGroup(sampleWithRequiredData);

        const medicationRequest = service.getMedicationRequest(formGroup) as any;

        expect(medicationRequest).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMedicationRequest should not enable id FormControl', () => {
        const formGroup = service.createMedicationRequestFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMedicationRequest should disable id FormControl', () => {
        const formGroup = service.createMedicationRequestFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
