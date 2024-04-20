import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../type-diseases.test-samples';

import { TypeDiseasesFormService } from './type-diseases-form.service';

describe('TypeDiseases Form Service', () => {
  let service: TypeDiseasesFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TypeDiseasesFormService);
  });

  describe('Service methods', () => {
    describe('createTypeDiseasesFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTypeDiseasesFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            diseasesType: expect.any(Object),
          }),
        );
      });

      it('passing ITypeDiseases should create a new form with FormGroup', () => {
        const formGroup = service.createTypeDiseasesFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            diseasesType: expect.any(Object),
          }),
        );
      });
    });

    describe('getTypeDiseases', () => {
      it('should return NewTypeDiseases for default TypeDiseases initial value', () => {
        const formGroup = service.createTypeDiseasesFormGroup(sampleWithNewData);

        const typeDiseases = service.getTypeDiseases(formGroup) as any;

        expect(typeDiseases).toMatchObject(sampleWithNewData);
      });

      it('should return NewTypeDiseases for empty TypeDiseases initial value', () => {
        const formGroup = service.createTypeDiseasesFormGroup();

        const typeDiseases = service.getTypeDiseases(formGroup) as any;

        expect(typeDiseases).toMatchObject({});
      });

      it('should return ITypeDiseases', () => {
        const formGroup = service.createTypeDiseasesFormGroup(sampleWithRequiredData);

        const typeDiseases = service.getTypeDiseases(formGroup) as any;

        expect(typeDiseases).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITypeDiseases should not enable id FormControl', () => {
        const formGroup = service.createTypeDiseasesFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTypeDiseases should disable id FormControl', () => {
        const formGroup = service.createTypeDiseasesFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
