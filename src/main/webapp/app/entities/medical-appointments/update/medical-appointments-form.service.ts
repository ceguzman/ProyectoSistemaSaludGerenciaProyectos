import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IMedicalAppointments, NewMedicalAppointments } from '../medical-appointments.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMedicalAppointments for edit and NewMedicalAppointmentsFormGroupInput for create.
 */
type MedicalAppointmentsFormGroupInput = IMedicalAppointments | PartialWithRequiredKeyOf<NewMedicalAppointments>;

type MedicalAppointmentsFormDefaults = Pick<NewMedicalAppointments, 'id'>;

type MedicalAppointmentsFormGroupContent = {
  id: FormControl<IMedicalAppointments['id'] | NewMedicalAppointments['id']>;
  dateMedical: FormControl<IMedicalAppointments['dateMedical']>;
  typeSpecialist: FormControl<IMedicalAppointments['typeSpecialist']>;
};

export type MedicalAppointmentsFormGroup = FormGroup<MedicalAppointmentsFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MedicalAppointmentsFormService {
  createMedicalAppointmentsFormGroup(medicalAppointments: MedicalAppointmentsFormGroupInput = { id: null }): MedicalAppointmentsFormGroup {
    const medicalAppointmentsRawValue = {
      ...this.getFormDefaults(),
      ...medicalAppointments,
    };
    return new FormGroup<MedicalAppointmentsFormGroupContent>({
      id: new FormControl(
        { value: medicalAppointmentsRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      dateMedical: new FormControl(medicalAppointmentsRawValue.dateMedical, {
        validators: [Validators.required],
      }),
      typeSpecialist: new FormControl(medicalAppointmentsRawValue.typeSpecialist, {
        validators: [Validators.required],
      }),
    });
  }

  getMedicalAppointments(form: MedicalAppointmentsFormGroup): IMedicalAppointments | NewMedicalAppointments {
    return form.getRawValue() as IMedicalAppointments | NewMedicalAppointments;
  }

  resetForm(form: MedicalAppointmentsFormGroup, medicalAppointments: MedicalAppointmentsFormGroupInput): void {
    const medicalAppointmentsRawValue = { ...this.getFormDefaults(), ...medicalAppointments };
    form.reset(
      {
        ...medicalAppointmentsRawValue,
        id: { value: medicalAppointmentsRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MedicalAppointmentsFormDefaults {
    return {
      id: null,
    };
  }
}
