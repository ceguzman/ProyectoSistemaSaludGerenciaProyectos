import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IMedicalAuthorization, NewMedicalAuthorization } from '../medical-authorization.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMedicalAuthorization for edit and NewMedicalAuthorizationFormGroupInput for create.
 */
type MedicalAuthorizationFormGroupInput = IMedicalAuthorization | PartialWithRequiredKeyOf<NewMedicalAuthorization>;

type MedicalAuthorizationFormDefaults = Pick<NewMedicalAuthorization, 'id'>;

type MedicalAuthorizationFormGroupContent = {
  id: FormControl<IMedicalAuthorization['id'] | NewMedicalAuthorization['id']>;
  detailAuthorization: FormControl<IMedicalAuthorization['detailAuthorization']>;
  stateAuthorization: FormControl<IMedicalAuthorization['stateAuthorization']>;
  dateAuthorization: FormControl<IMedicalAuthorization['dateAuthorization']>;
  clinicHistory: FormControl<IMedicalAuthorization['clinicHistory']>;
};

export type MedicalAuthorizationFormGroup = FormGroup<MedicalAuthorizationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MedicalAuthorizationFormService {
  createMedicalAuthorizationFormGroup(
    medicalAuthorization: MedicalAuthorizationFormGroupInput = { id: null },
  ): MedicalAuthorizationFormGroup {
    const medicalAuthorizationRawValue = {
      ...this.getFormDefaults(),
      ...medicalAuthorization,
    };
    return new FormGroup<MedicalAuthorizationFormGroupContent>({
      id: new FormControl(
        { value: medicalAuthorizationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      detailAuthorization: new FormControl(medicalAuthorizationRawValue.detailAuthorization, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      stateAuthorization: new FormControl(medicalAuthorizationRawValue.stateAuthorization, {
        validators: [Validators.required],
      }),
      dateAuthorization: new FormControl(medicalAuthorizationRawValue.dateAuthorization, {
        validators: [Validators.required],
      }),
      clinicHistory: new FormControl(medicalAuthorizationRawValue.clinicHistory, {
        validators: [Validators.required],
      }),
    });
  }

  getMedicalAuthorization(form: MedicalAuthorizationFormGroup): IMedicalAuthorization | NewMedicalAuthorization {
    return form.getRawValue() as IMedicalAuthorization | NewMedicalAuthorization;
  }

  resetForm(form: MedicalAuthorizationFormGroup, medicalAuthorization: MedicalAuthorizationFormGroupInput): void {
    const medicalAuthorizationRawValue = { ...this.getFormDefaults(), ...medicalAuthorization };
    form.reset(
      {
        ...medicalAuthorizationRawValue,
        id: { value: medicalAuthorizationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MedicalAuthorizationFormDefaults {
    return {
      id: null,
    };
  }
}
