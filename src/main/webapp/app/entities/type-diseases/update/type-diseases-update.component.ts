import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITypeDiseases } from '../type-diseases.model';
import { TypeDiseasesService } from '../service/type-diseases.service';
import { TypeDiseasesFormService, TypeDiseasesFormGroup } from './type-diseases-form.service';

@Component({
  standalone: true,
  selector: 'jhi-type-diseases-update',
  templateUrl: './type-diseases-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TypeDiseasesUpdateComponent implements OnInit {
  isSaving = false;
  typeDiseases: ITypeDiseases | null = null;

  protected typeDiseasesService = inject(TypeDiseasesService);
  protected typeDiseasesFormService = inject(TypeDiseasesFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TypeDiseasesFormGroup = this.typeDiseasesFormService.createTypeDiseasesFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ typeDiseases }) => {
      this.typeDiseases = typeDiseases;
      if (typeDiseases) {
        this.updateForm(typeDiseases);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const typeDiseases = this.typeDiseasesFormService.getTypeDiseases(this.editForm);
    if (typeDiseases.id !== null) {
      this.subscribeToSaveResponse(this.typeDiseasesService.update(typeDiseases));
    } else {
      this.subscribeToSaveResponse(this.typeDiseasesService.create(typeDiseases));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITypeDiseases>>): void {
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

  protected updateForm(typeDiseases: ITypeDiseases): void {
    this.typeDiseases = typeDiseases;
    this.typeDiseasesFormService.resetForm(this.editForm, typeDiseases);
  }
}
