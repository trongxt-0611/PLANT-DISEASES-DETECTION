# model.py

class DiseaseSolution(BaseModel):
    id: Optional[PydanticObjectId] = Field(None, alias="_id")
    slug: str
    name: str
    ingredients: List[Ingredient]
    instructions: List[str]
    date_added: Optional[datetime]
    date_updated: Optional[datetime]
    def to_json(self):
        return jsonable_encoder(self, exclude_none=True)
    def to_bson(self):
        data = self.dict(by_alias=True, exclude_none=True)
        if data["_id"] is None:
            data.pop("_id")
        return data