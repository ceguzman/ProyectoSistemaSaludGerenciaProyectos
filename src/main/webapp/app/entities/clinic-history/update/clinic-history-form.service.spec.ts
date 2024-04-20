import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../clinic-history.test-samples';

import { ClinicHistoryFormService } from './clinic-history-form.service';

describe('ClinicHistory Form Service', () => {
  let service: ClinicHistoryFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ClinicHistoryFormService);
  });

  describe('Service methods', () => {
    describe('createClinicHistoryFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createClinicHistoryFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dateClinic: expect.any(Object),
            typeDisease: expect.any(Object),
            people: expect.any(Object),
          }),
        );
      });

      it('passing IClinicHistory should create a new form with FormGroup', () => {
        const formGroup = service.createClinicHistoryFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dateClinic: expect.any(Object),
            typeDisease: expect.any(Object),
            people: expect.any(Object),
          }),
        );
      });
    });

    describe('getClinicHistory', () => {
      it('should return NewClinicHistory for default ClinicHistory initial value', () => {
        const formGroup = service.createClinicHistoryFormGroup(sampleWithNewData);

        const clinicHistory = service.getClinicHistory(formGroup) as any;

        expect(clinicHistory).toMatchObject(sampleWithNewData);
      });

      it('should return NewClinicHistory for empty ClinicHistory initial value', () => {
        const formGroup = service.createClinicHistoryFormGroup();

        const clinicHistory = service.getClinicHistory(formGroup) as any;

        expect(clinicHistory).toMatchObject({});
      });

      it('should return IClinicHistory', () => {
        const formGroup = service.createClinicHistoryFormGroup(sampleWithRequiredData);

        const clinicHistory = service.getClinicHistory(formGroup) as any;

        expect(clinicHistory).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IClinicHistory should not enable id FormControl', () => {
        const formGroup = service.createClinicHistoryFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewClinicHistory should disable id FormControl', () => {
        const formGroup = service.createClinicHistoryFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
