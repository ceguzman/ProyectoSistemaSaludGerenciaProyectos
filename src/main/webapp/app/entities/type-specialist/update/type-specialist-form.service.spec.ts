import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../type-specialist.test-samples';

import { TypeSpecialistFormService } from './type-specialist-form.service';

describe('TypeSpecialist Form Service', () => {
  let service: TypeSpecialistFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TypeSpecialistFormService);
  });

  describe('Service methods', () => {
    describe('createTypeSpecialistFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTypeSpecialistFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            specialistType: expect.any(Object),
            stateSpecialist: expect.any(Object),
          }),
        );
      });

      it('passing ITypeSpecialist should create a new form with FormGroup', () => {
        const formGroup = service.createTypeSpecialistFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            specialistType: expect.any(Object),
            stateSpecialist: expect.any(Object),
          }),
        );
      });
    });

    describe('getTypeSpecialist', () => {
      it('should return NewTypeSpecialist for default TypeSpecialist initial value', () => {
        const formGroup = service.createTypeSpecialistFormGroup(sampleWithNewData);

        const typeSpecialist = service.getTypeSpecialist(formGroup) as any;

        expect(typeSpecialist).toMatchObject(sampleWithNewData);
      });

      it('should return NewTypeSpecialist for empty TypeSpecialist initial value', () => {
        const formGroup = service.createTypeSpecialistFormGroup();

        const typeSpecialist = service.getTypeSpecialist(formGroup) as any;

        expect(typeSpecialist).toMatchObject({});
      });

      it('should return ITypeSpecialist', () => {
        const formGroup = service.createTypeSpecialistFormGroup(sampleWithRequiredData);

        const typeSpecialist = service.getTypeSpecialist(formGroup) as any;

        expect(typeSpecialist).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITypeSpecialist should not enable id FormControl', () => {
        const formGroup = service.createTypeSpecialistFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTypeSpecialist should disable id FormControl', () => {
        const formGroup = service.createTypeSpecialistFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
