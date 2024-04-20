import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IMedicationRequest, NewMedicationRequest } from '../medication-request.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMedicationRequest for edit and NewMedicationRequestFormGroupInput for create.
 */
type MedicationRequestFormGroupInput = IMedicationRequest | PartialWithRequiredKeyOf<NewMedicationRequest>;

type MedicationRequestFormDefaults = Pick<NewMedicationRequest, 'id'>;

type MedicationRequestFormGroupContent = {
  id: FormControl<IMedicationRequest['id'] | NewMedicationRequest['id']>;
  name: FormControl<IMedicationRequest['name']>;
  amount: FormControl<IMedicationRequest['amount']>;
  milligrams: FormControl<IMedicationRequest['milligrams']>;
  medicalAuthorization: FormControl<IMedicationRequest['medicalAuthorization']>;
};

export type MedicationRequestFormGroup = FormGroup<MedicationRequestFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MedicationRequestFormService {
  createMedicationRequestFormGroup(medicationRequest: MedicationRequestFormGroupInput = { id: null }): MedicationRequestFormGroup {
    const medicationRequestRawValue = {
      ...this.getFormDefaults(),
      ...medicationRequest,
    };
    return new FormGroup<MedicationRequestFormGroupContent>({
      id: new FormControl(
        { value: medicationRequestRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(medicationRequestRawValue.name, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      amount: new FormControl(medicationRequestRawValue.amount, {
        validators: [Validators.required, Validators.max(10)],
      }),
      milligrams: new FormControl(medicationRequestRawValue.milligrams, {
        validators: [Validators.max(10)],
      }),
      medicalAuthorization: new FormControl(medicationRequestRawValue.medicalAuthorization, {
        validators: [Validators.required],
      }),
    });
  }

  getMedicationRequest(form: MedicationRequestFormGroup): IMedicationRequest | NewMedicationRequest {
    return form.getRawValue() as IMedicationRequest | NewMedicationRequest;
  }

  resetForm(form: MedicationRequestFormGroup, medicationRequest: MedicationRequestFormGroupInput): void {
    const medicationRequestRawValue = { ...this.getFormDefaults(), ...medicationRequest };
    form.reset(
      {
        ...medicationRequestRawValue,
        id: { value: medicationRequestRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MedicationRequestFormDefaults {
    return {
      id: null,
    };
  }
}
