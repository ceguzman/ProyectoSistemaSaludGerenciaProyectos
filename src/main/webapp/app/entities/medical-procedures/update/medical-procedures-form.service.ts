import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IMedicalProcedures, NewMedicalProcedures } from '../medical-procedures.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMedicalProcedures for edit and NewMedicalProceduresFormGroupInput for create.
 */
type MedicalProceduresFormGroupInput = IMedicalProcedures | PartialWithRequiredKeyOf<NewMedicalProcedures>;

type MedicalProceduresFormDefaults = Pick<NewMedicalProcedures, 'id'>;

type MedicalProceduresFormGroupContent = {
  id: FormControl<IMedicalProcedures['id'] | NewMedicalProcedures['id']>;
  typeProcedures: FormControl<IMedicalProcedures['typeProcedures']>;
  description: FormControl<IMedicalProcedures['description']>;
  dateProcedures: FormControl<IMedicalProcedures['dateProcedures']>;
  medicalAuthorization: FormControl<IMedicalProcedures['medicalAuthorization']>;
};

export type MedicalProceduresFormGroup = FormGroup<MedicalProceduresFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MedicalProceduresFormService {
  createMedicalProceduresFormGroup(medicalProcedures: MedicalProceduresFormGroupInput = { id: null }): MedicalProceduresFormGroup {
    const medicalProceduresRawValue = {
      ...this.getFormDefaults(),
      ...medicalProcedures,
    };
    return new FormGroup<MedicalProceduresFormGroupContent>({
      id: new FormControl(
        { value: medicalProceduresRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      typeProcedures: new FormControl(medicalProceduresRawValue.typeProcedures, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      description: new FormControl(medicalProceduresRawValue.description, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      dateProcedures: new FormControl(medicalProceduresRawValue.dateProcedures, {
        validators: [Validators.required],
      }),
      medicalAuthorization: new FormControl(medicalProceduresRawValue.medicalAuthorization, {
        validators: [Validators.required],
      }),
    });
  }

  getMedicalProcedures(form: MedicalProceduresFormGroup): IMedicalProcedures | NewMedicalProcedures {
    return form.getRawValue() as IMedicalProcedures | NewMedicalProcedures;
  }

  resetForm(form: MedicalProceduresFormGroup, medicalProcedures: MedicalProceduresFormGroupInput): void {
    const medicalProceduresRawValue = { ...this.getFormDefaults(), ...medicalProcedures };
    form.reset(
      {
        ...medicalProceduresRawValue,
        id: { value: medicalProceduresRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MedicalProceduresFormDefaults {
    return {
      id: null,
    };
  }
}
