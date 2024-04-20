import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IMedicalAuthorization } from 'app/entities/medical-authorization/medical-authorization.model';
import { MedicalAuthorizationService } from 'app/entities/medical-authorization/service/medical-authorization.service';
import { IMedicalProcedures } from '../medical-procedures.model';
import { MedicalProceduresService } from '../service/medical-procedures.service';
import { MedicalProceduresFormService, MedicalProceduresFormGroup } from './medical-procedures-form.service';

@Component({
  standalone: true,
  selector: 'jhi-medical-procedures-update',
  templateUrl: './medical-procedures-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MedicalProceduresUpdateComponent implements OnInit {
  isSaving = false;
  medicalProcedures: IMedicalProcedures | null = null;

  medicalAuthorizationsSharedCollection: IMedicalAuthorization[] = [];

  protected medicalProceduresService = inject(MedicalProceduresService);
  protected medicalProceduresFormService = inject(MedicalProceduresFormService);
  protected medicalAuthorizationService = inject(MedicalAuthorizationService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MedicalProceduresFormGroup = this.medicalProceduresFormService.createMedicalProceduresFormGroup();

  compareMedicalAuthorization = (o1: IMedicalAuthorization | null, o2: IMedicalAuthorization | null): boolean =>
    this.medicalAuthorizationService.compareMedicalAuthorization(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ medicalProcedures }) => {
      this.medicalProcedures = medicalProcedures;
      if (medicalProcedures) {
        this.updateForm(medicalProcedures);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const medicalProcedures = this.medicalProceduresFormService.getMedicalProcedures(this.editForm);
    if (medicalProcedures.id !== null) {
      this.subscribeToSaveResponse(this.medicalProceduresService.update(medicalProcedures));
    } else {
      this.subscribeToSaveResponse(this.medicalProceduresService.create(medicalProcedures));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMedicalProcedures>>): void {
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

  protected updateForm(medicalProcedures: IMedicalProcedures): void {
    this.medicalProcedures = medicalProcedures;
    this.medicalProceduresFormService.resetForm(this.editForm, medicalProcedures);

    this.medicalAuthorizationsSharedCollection =
      this.medicalAuthorizationService.addMedicalAuthorizationToCollectionIfMissing<IMedicalAuthorization>(
        this.medicalAuthorizationsSharedCollection,
        medicalProcedures.medicalAuthorization,
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
            this.medicalProcedures?.medicalAuthorization,
          ),
        ),
      )
      .subscribe((medicalAuthorizations: IMedicalAuthorization[]) => (this.medicalAuthorizationsSharedCollection = medicalAuthorizations));
  }
}
