import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IMedicalAuthorization } from 'app/entities/medical-authorization/medical-authorization.model';
import { MedicalAuthorizationService } from 'app/entities/medical-authorization/service/medical-authorization.service';
import { IMedicationRequest } from '../medication-request.model';
import { MedicationRequestService } from '../service/medication-request.service';
import { MedicationRequestFormService, MedicationRequestFormGroup } from './medication-request-form.service';

@Component({
  standalone: true,
  selector: 'jhi-medication-request-update',
  templateUrl: './medication-request-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MedicationRequestUpdateComponent implements OnInit {
  isSaving = false;
  medicationRequest: IMedicationRequest | null = null;

  medicalAuthorizationsSharedCollection: IMedicalAuthorization[] = [];

  protected medicationRequestService = inject(MedicationRequestService);
  protected medicationRequestFormService = inject(MedicationRequestFormService);
  protected medicalAuthorizationService = inject(MedicalAuthorizationService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MedicationRequestFormGroup = this.medicationRequestFormService.createMedicationRequestFormGroup();

  compareMedicalAuthorization = (o1: IMedicalAuthorization | null, o2: IMedicalAuthorization | null): boolean =>
    this.medicalAuthorizationService.compareMedicalAuthorization(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ medicationRequest }) => {
      this.medicationRequest = medicationRequest;
      if (medicationRequest) {
        this.updateForm(medicationRequest);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const medicationRequest = this.medicationRequestFormService.getMedicationRequest(this.editForm);
    if (medicationRequest.id !== null) {
      this.subscribeToSaveResponse(this.medicationRequestService.update(medicationRequest));
    } else {
      this.subscribeToSaveResponse(this.medicationRequestService.create(medicationRequest));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMedicationRequest>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(medicationRequest: IMedicationRequest): void {
    this.medicationRequest = medicationRequest;
    this.medicationRequestFormService.resetForm(this.editForm, medicationRequest);

    this.medicalAuthorizationsSharedCollection =
      this.medicalAuthorizationService.addMedicalAuthorizationToCollectionIfMissing<IMedicalAuthorization>(
        this.medicalAuthorizationsSharedCollection,
        medicationRequest.medicalAuthorization,
      );
  }

  protected loadRelationshipsOptions(): void {
    this.medicalAuthorizationService
      .query()
      .pipe(map((res: HttpResponse<IMedicalAuthorization[]>) => res.body ?? []))
      .pipe(
        map((medicalAuthorizations: IMedicalAuthorization[]) =>
          this.medicalAuthorizationService.addMedicalAuthorizationToCollectionIfMissing<IMedicalAuthorization>(
            medicalAuthorizations,
            this.medicationRequest?.medicalAuthorization,
          ),
        ),
      )
      .subscribe((medicalAuthorizations: IMedicalAuthorization[]) => (this.medicalAuthorizationsSharedCollection = medicalAuthorizations));
  }
}
